pipeline {
    agent any

    environment {
        DOCKER_USER = 'blendigr'
        DOCKER_REPO = 'online-flashcards'
        IMAGE_TAG   = "${env.BUILD_NUMBER}"
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
                            bat 'npm run test:coverage'
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

        stage('Build & Push Docker Images') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'docker_hub', usernameVariable: 'USER', passwordVariable: 'PASS')]) {
                    bat """
                        docker login -u %USER% -p %PASS%
                        docker compose build

                        @echo Tagging and Pushing...
                        
                        // Use underscore to match the folder name in your Jenkins workspace
                        docker tag online_flashcards-backend %USER%/%DOCKER_REPO%-backend:%IMAGE_TAG%
                        docker tag online_flashcards-frontend %USER%/%DOCKER_REPO%-frontend:%IMAGE_TAG%
                        docker tag online_flashcards-backend %USER%/%DOCKER_REPO%-backend:latest
                        docker tag online_flashcards-frontend %USER%/%DOCKER_REPO%-frontend:latest
                        
                        docker push %USER%/%DOCKER_REPO%-backend:%IMAGE_TAG%
                        docker push %USER%/%DOCKER_REPO%-frontend:%IMAGE_TAG%
                        docker push %USER%/%DOCKER_REPO%-backend:latest
                        docker push %USER%/%DOCKER_REPO%-frontend:latest
                    """
                }
            }
        }

        stage('Deploy Local') {
            steps {
                bat 'docker compose up -d'
                echo "App is live at http://localhost:5173"
            }
        }
    }

    post {
        success {
            echo 'Pipeline completed successfully! Containers are running.'
        }
        failure {
            echo 'Pipeline failed. Cleaning up...'
            bat 'docker compose down -v || exit 0'
            cleanWs()
        }
    }
}