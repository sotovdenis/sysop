pipeline {
    agent any

    tools {
        maven 'maven-3.9.9'
    }

    environment {
        JAVA_HOME = tool 'jdk-21'
        PATH = "${JAVA_HOME}/bin:${PATH}"
    }

    stages {
        stage('Checkout') {
            steps {
                sh 'which java || true'
                sh 'java -version'
                sh 'javac -version'
                sh 'echo "JAVA_HOME=$JAVA_HOME"'
                sh 'mvn -v'
                checkout scm
                sh 'git submodule init || true'
                sh 'git submodule update --recursive --remote || true'
            }
        }

        stage('Build') {
            steps {
                sh 'mvn -B clean install -DskipTests'
            }
        }

        stage('Docker Compose Build') {
            steps {
                script {
                            // Убедитесь, что Docker и docker-compose доступны
                    sh 'docker --version'
                    sh 'docker compose --version || docker compose version'

                            // Удалить старые контейнеры и образы (опционально)
                    sh 'docker compose -f docker-compose.app.yml down --remove-orphans || true'

                            // Пересобрать образы
                    sh 'docker compose -f docker-compose.app.yml build --no-cache'

                            // Запустить контейнеры (если нужно)
                    sh 'docker compose -f docker-compose.app.yml up -d'
                }
            }
        }

        stage('Deploy') {
            steps {
                sh 'pwd'
                sh 'ls -la'
                sh 'ls -la prometheus/'

                sh 'test -f prometheus/prometheus.yml && echo "✅ prometheus.yml FOUND" || echo "❌ prometheus.yml MISSING"'
                sh 'docker-compose down'
                sh 'docker-compose up --build -d'
            }
        }
    }
}