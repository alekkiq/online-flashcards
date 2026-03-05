pipeline {
    agent any

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

        stage('Build & Test') {
            parallel {
                stage('Backend') {
                    steps {
                        bat 'docker compose up -d --wait db'
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

        stage('Build Docker Images') {
            steps {
                bat 'docker compose build'
            }
        }
    }

    post {
        always {
            bat 'docker compose down -v || exit 0'
            cleanWs() 
        }
        success {
            echo 'Pipeline completed successfully!'
        }
        failure {
            echo 'Pipeline failed.'
        }
    }
}