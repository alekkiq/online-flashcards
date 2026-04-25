pipeline {
    agent any

    parameters {
        string(name: 'DOCKER_USER', defaultValue: 'dockeruser', description: 'Docker Hub username for pushing images')
        string(name: 'DOCKER_CREDENTIAL_ID', defaultValue: 'Docker_Hub', description: 'Jenkins credential ID for Docker Hub')
    }

    environment {
        DOCKER_USER = "${params.DOCKER_USER}"
        DOCKER_REPO = "online-flashcards"
        IMAGE_TAG = "${env.BUILD_NUMBER}"
    }

    tools {
        nodejs 'node-20'
        jdk 'jdk-21'
    }

    stages {
        stage('Setup & Checkout') {
            steps {
                checkout scm
                withCredentials([file(credentialsId: 'flashcards-env', variable: 'ENV_FILE')]) {
                    bat 'copy "%ENV_FILE%" .env'
                }
            }
        }

        stage('Database') {
            steps {
                bat 'docker compose up -d --wait db'
            }
        }

        stage('Build & Test') {
            parallel {
                stage('Backend') {
                    steps {
                        dir('backend') {
                            bat 'mvnw.cmd clean package'
                        }
                    }
                    post {
                        always {
                            junit 'backend/target/surefire-reports/*.xml'
                            jacoco execPattern: 'backend/target/jacoco.exec'
                        }
                    }
                }

                stage('Frontend') {
                    steps {
                        dir('frontend') {
                            bat 'npm ci'
                            // bat 'npm run test:coverage'
                            bat 'npm run build'
                        }
                    }
                    post {
                        always {
                            publishHTML(target: [
                                reportName:   'Frontend Coverage',
                                reportDir:    'frontend/coverage',
                                reportFiles:  'index.html',
                                keepAll:      true,
                                allowMissing: true
                            ])
                        }
                    }
                }
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('SonarQubeServer') {
                    dir('backend') {
                        bat "\"${tool 'SonarScanner'}\\bin\\sonar-scanner\""
                    }
                }
            }
        }

        stage('Build & Push Docker Images') {
            steps {
                withCredentials([usernamePassword(credentialsId: params.DOCKER_CREDENTIAL_ID, usernameVariable: 'USER', passwordVariable: 'PASS')]) {
                    bat """
                    @echo Logging in to Docker Hub...
                    docker login -u %USER% -p %PASS%

                    @echo Building backend image...
                    docker build -t %DOCKER_USER%/online-flashcards-backend:%IMAGE_TAG% -t %DOCKER_USER%/online-flashcards-backend:latest ./backend

                    @echo Building frontend image...
                    docker build -t %DOCKER_USER%/online-flashcards-frontend:%IMAGE_TAG% -t %DOCKER_USER%/online-flashcards-frontend:latest ./frontend

                    @echo Pushing Docker images...
                    docker push %DOCKER_USER%/online-flashcards-backend:%IMAGE_TAG%
                    docker push %DOCKER_USER%/online-flashcards-backend:latest
                    docker push %DOCKER_USER%/online-flashcards-frontend:%IMAGE_TAG%
                    docker push %DOCKER_USER%/online-flashcards-frontend:latest

                    @echo Removing local images to free resources...
                    docker rmi %DOCKER_USER%/online-flashcards-backend:%IMAGE_TAG% || exit 0
                    docker rmi %DOCKER_USER%/online-flashcards-backend:latest || exit 0
                    docker rmi %DOCKER_USER%/online-flashcards-frontend:%IMAGE_TAG% || exit 0
                    docker rmi %DOCKER_USER%/online-flashcards-frontend:latest || exit 0
                    """
                }
            }
        }

    }

    post {
        always {
            echo 'Cleaning up Docker resources...'
            bat '''
            docker compose down -v --remove-orphans || exit 0
            docker image prune -f || exit 0
            docker builder prune -f || exit 0
            '''
        }
        success {
            echo 'Pipeline completed successfully! Images pushed to Docker Hub.'
        }
        failure {
            echo 'Pipeline failed.'
            cleanWs()
        }
    }
}