pipeline {
    agent any

    tools {
        jdk 'jdk-21'
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
                    sh '''
                        cat > .env <<EOF
                        MYSQL_HOST=localhost
                        MYSQL_PORT=3306
                        MYSQL_DATABASE=${MYSQL_DATABASE}
                        MYSQL_USER=${MYSQL_USER}
                        MYSQL_PASSWORD=${MYSQL_PASSWORD}
                        MYSQL_ROOT_PASSWORD=${MYSQL_ROOT_PASSWORD}
                        API_PORT=${API_PORT}
                        JWT_SECRET=${JWT_SECRET}
                        JWT_EXPIRATION=${JWT_EXPIRATION}
                        EOF
                    '''
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
