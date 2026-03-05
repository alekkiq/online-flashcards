pipeline {
    agent any

    tools {
        nodejs 'node-20'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Generate .env') {
            steps {
                withCredentials([
                    string(credentialsId: 'flashcards-db-name',          variable: 'MYSQL_DATABASE'),
                    string(credentialsId: 'flashcards-db-user',          variable: 'MYSQL_USER'),
                    string(credentialsId: 'flashcards-db-password',      variable: 'MYSQL_PASSWORD'),
                    string(credentialsId: 'flashcards-db-root-password', variable: 'MYSQL_ROOT_PASSWORD'),
                    string(credentialsId: 'flashcards-jwt-secret',       variable: 'JWT_SECRET'),
                    string(credentialsId: 'flashcards-jwt-expiration',   variable: 'JWT_EXPIRATION'),
                    string(credentialsId: 'flashcards-api-port',         variable: 'API_PORT')
                ]) {
                    bat """
                        @echo off
                        (
                            echo MYSQL_HOST=localhost
                            echo MYSQL_PORT=3306
                            echo MYSQL_DATABASE=%MYSQL_DATABASE%
                            echo MYSQL_USER=%MYSQL_USER%
                            echo MYSQL_PASSWORD=%MYSQL_PASSWORD%
                            echo MYSQL_ROOT_PASSWORD=%MYSQL_ROOT_PASSWORD%
                            echo API_PORT=%API_PORT%
                            echo JWT_SECRET=%JWT_SECRET%
                            echo JWT_EXPIRATION=%JWT_EXPIRATION%
                        ) > .env
                    """
                }
            }
        }

        stage('Backend') {
            stages {
                stage('Start Database') {
                    steps {
                        bat 'docker compose up -d --wait db'
                    }
                }
                stage('Build & Test Backend') {
                    steps {
                        dir('backend') {
                            bat '''
                                @echo off
                                for /f "usebackq tokens=1,2 delims==" %%a in ("..\\.env") do set "%%a=%%b"
                                mvnw.cmd clean package
                            '''
                        }
                    }
                    post {
                        always {
                            junit 'backend/target/surefire-reports/*.xml'
                            jacoco execPattern: 'backend/target/jacoco.exec'
                        }
                    }
                }
            }
        }

        stage('Frontend') {
            stages {
                stage('Install Dependencies') {
                    steps {
                        dir('frontend') {
                            bat 'npm ci'
                        }
                    }
                }
                stage('Test & Coverage Frontend') {
                    steps {
                        dir('frontend') {
                            bat 'npm run test:coverage'
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
                stage('Build Frontend') {
                    steps {
                        dir('frontend') {
                            bat 'npm run build'
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
            bat 'del /f /q .env 2>nul || exit 0'
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
