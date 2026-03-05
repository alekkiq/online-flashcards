pipeline {
    agent any

    tools {
        jdk 'jdk-21'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Generate .env') {
            steps {
                withCredentials([file(credentialsId: 'flashcards-env', variable: 'ENV_FILE')]) {
                    bat 'copy "%ENV_FILE%" .env'
                }
            }
        }

        stage('Backend') {
            stages {
                stage('Start Database') {
                    steps {
                        bat 'docker compose up -d db'
                        bat 'docker compose exec db mariadb -u root -p%MYSQL_ROOT_PASSWORD% -e "SELECT 1" --wait'
                    }
                }
                stage('Build & Test Backend') {
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
                                reportName:  'Frontend Coverage',
                                reportDir:   'frontend/coverage',
                                reportFiles: 'index.html',
                                keepAll:     true
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
            bat 'del .env 2>nul || exit 0'
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
