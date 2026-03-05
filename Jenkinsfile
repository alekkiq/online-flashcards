pipeline {
    agent any

    tools {
        nodejs 'node-20'
    }

    environment {
        MYSQL_HOST          = 'localhost'
        MYSQL_PORT          = '3306'
        MYSQL_DATABASE      = credentials('flashcards-db-name')
        MYSQL_USER          = credentials('flashcards-db-user')
        MYSQL_PASSWORD      = credentials('flashcards-db-password')
        MYSQL_ROOT_PASSWORD = credentials('flashcards-db-root-password')
        JWT_SECRET          = credentials('flashcards-jwt-secret')
        JWT_EXPIRATION      = credentials('flashcards-jwt-expiration')
        API_PORT            = credentials('flashcards-api-port')
    }

    stages {
        stage('Setup & Checkout') {
            steps {
                checkout scm
                writeFile file: '.env', text: """
                    MYSQL_HOST=${MYSQL_HOST}
                    MYSQL_PORT=${MYSQL_PORT}
                    MYSQL_DATABASE=${MYSQL_DATABASE}
                    MYSQL_USER=${MYSQL_USER}
                    MYSQL_PASSWORD=${MYSQL_PASSWORD}
                    MYSQL_ROOT_PASSWORD=${MYSQL_ROOT_PASSWORD}
                    API_PORT=${API_PORT}
                    JWT_SECRET=${JWT_SECRET}
                    JWT_EXPIRATION=${JWT_EXPIRATION}
                """.stripIndent()
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