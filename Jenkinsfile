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


        stage('Fix prometheus.yml') {
            steps {
                sh '''
                if [ -d prometheus.yml ]; then
                echo "❌ prometheus.yml is directory — removing"
                rm -rf prometheus.yml
                fi
                '''
            }
        }

                        stage('Prepare Prometheus config') {
                            steps {
                                sh '''
                                # Удаляем, если вдруг осталась директория в корне
                                if [ -e prometheus.yml ]; then
                                    rm -rf prometheus.yml
                                fi

                                if [ ! -f prometheus.yml ] || [ ! -s prometheus.yml ]; then
                                    echo "✅ Создаём prometheus.yml в корне проекта"
                                    cat > prometheus.yml << 'EOF'
                        global:
                          scrape_interval: 5s

                        scrape_configs:
                          - job_name: "sop"
                            metrics_path: "/actuator/prometheus"
                            static_configs:
                              - targets:
                                  [
                                    "games-swap-service:8080",
                                    "simple-notification-service:8083",
                                    "audit-service:8082",
                                    "analytics-service:8081"
                                  ]
                        EOF
                                else
                                    echo "✅ prometheus.yml уже существует в корне"
                                fi
                                '''
                            }
                        }

                        stage('Debug: Show prometheus.yml') {
                            steps {
                                sh '''
                                echo "=== Содержимое корня ==="
                                ls -la
                                echo "=== Тип prometheus.yml ==="
                                if [ -e prometheus.yml ]; then
                                    stat prometheus.yml
                                else
                                    echo "❌ prometheus.yml не существует в корне"
                                fi
                                echo "=== Содержимое папки prometheus/ ==="
                                ls -la prometheus/
                                '''
                            }
                        }


        stage('Docker Compose Build') {
                    steps {
                        script {
                            sh 'docker --version'

                            sh 'docker compose --version || docker compose version'

                            sh 'docker compose -f docker-compose.app.yml down --remove-orphans || true'

                            sh 'docker compose -f docker-compose.app.yml build --no-cache'

                            sh 'docker compose -f docker-compose.app.yml up -d'
                        }
                    }
                }



//         stage('Deploy') {
//             steps {
//                 sh 'pwd'
//                 sh 'ls -la'
//                 sh 'ls -la prometheus/'
//
//                 sh 'test -f prometheus.yml && echo "✅ prometheus.yml FOUND" || echo "❌ prometheus.yml MISSING"'
//                 sh 'docker compose -f docker-compose.app.yml down'
//                 sh 'docker compose -f docker-compose.app.yml up --build -d'
//             }
//         }
    }
}