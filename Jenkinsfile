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
                    sh 'cp "$ENV_FILE" .env'
                }
            }
        }

        stage('Backend') {
            stages {
                stage('Start Database') {
                    steps {
                        sh 'docker compose up -d db'
                        sh 'docker compose exec db mariadb -u root -p$(grep MYSQL_ROOT_PASSWORD .env | cut -d= -f2) -e "SELECT 1" --wait'
                    }
                }
                stage('Build & Test Backend') {
                    steps {
                        dir('backend') {
                            sh 'chmod +x mvnw'
                            sh 'set -a && . ../.env && set +a && ./mvnw clean package'
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
            agent {
                docker {
                    image 'node:20'
                    reuseNode true
                }
            }
            stages {
                stage('Install Dependencies') {
                    steps {
                        dir('frontend') {
                            sh 'npm ci'
                        }
                    }
                }
                stage('Test & Coverage Frontend') {
                    steps {
                        dir('frontend') {
                            sh 'npm run test:coverage'
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
                            sh 'npm run build'
                        }
                    }
                }
            }
        }

        stage('Build Docker Images') {
            steps {
                sh 'docker compose build'
            }
        }
    }

    post {
        always {
            sh 'docker compose down -v || true'
            sh 'rm -f .env'
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
