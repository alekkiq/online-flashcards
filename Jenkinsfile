pipeline {
    agent any

    environment {
        DOCKER_USER = 'aleksput' // LAITA TÄHÄ OMA NIMI SIT ET VOI TESTAA TYYLII OMAA BRANCHII TAI JOTAI
        /*
        MENE JENKINSSIIN JA TEE TUNNILLA OPETETUN TAVAN MUKAAN DOCKER HUB KÄYTTÄJÄ JA SALASANA TOKENILLA. SITTEN LAITA TOHON DOCKER_USER NIMI ja voit runaa sitten build.
        */
        DOCKER_REPO = 'online-flashcards'
        IMAGE_TAG   = "${env.BUILD_NUMBER}"
        DOCKER_HUB_CREDENTIAL_ID = "${env.DOCKER_HUB_CREDENTIAL_ID}"
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
                withCredentials([usernamePassword(credentialsId: DOCKER_HUB_CREDENTIAL_ID, usernameVariable: 'USER', passwordVariable: 'PASS')]) {
                    bat """
                        docker login -u %USER% -p %PASS%

                        @echo Building Docker images...
                        docker compose build

                        @echo Pushing Docker images...
                        docker push %DOCKER_USER%/online-flashcards-backend:%IMAGE_TAG%
                        docker push %DOCKER_USER%/online-flashcards-frontend:%IMAGE_TAG%
                        docker push %DOCKER_USER%/online-flashcards-backend:latest
                        docker push %DOCKER_USER%/online-flashcards-frontend:latest
                    """
                }
            }
        }

        stage('Deploy Local') {
            steps {
                bat 'docker compose up -d'
                echo "App is live at http://localhost:3000"
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