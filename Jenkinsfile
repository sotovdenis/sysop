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

        stage('Clean Prometheus Config') {
            steps {
                sh '''
                echo "Очищаем конфигурацию Prometheus..."
                # Удаляем директорию или файл
                rm -rf /var/jenkins_home/workspace/pipeline/prometheus/prometheus.yml
                rm -rf prometheus/prometheus.yml

                # Проверяем
                ls -la prometheus/ || true
                '''
            }
        }


                        stage('Prepare Prometheus config') {
                            steps {
                                sh '''
                                mkdir -p prometheus

                                if [ -d prometheus/prometheus.yml ]; then
                                    echo "❌ prometheus.yml — это директория, удаляем"
                                    rm -rf prometheus/prometheus.yml
                                fi

                                if [ ! -f prometheus/prometheus.yml ] || [ ! -s prometheus/prometheus.yml ]; then
                                    echo "✅ Создаём prometheus.yml"
                                    echo "global:" > prometheus/prometheus.yml
                                    echo "  scrape_interval: 5s" >> prometheus/prometheus.yml
                                    echo "" >> prometheus/prometheus.yml
                                    echo "scrape_configs:" >> prometheus/prometheus.yml
                                    echo "  - job_name: \\"sop\\"" >> prometheus/prometheus.yml
                                    echo "    metrics_path: \\"/actuator/prometheus\\"" >> prometheus/prometheus.yml
                                    echo "    static_configs:" >> prometheus/prometheus.yml
                                    echo "      - targets:" >> prometheus/prometheus.yml
                                    echo "          [" >> prometheus/prometheus.yml
                                    echo "            \\"games-swap-service:8080\\"," >> prometheus/prometheus.yml
                                    echo "            \\"simple-notification-service:8083\\"," >> prometheus/prometheus.yml
                                    echo "            \\"audit-service:8082\\"," >> prometheus/prometheus.yml
                                    echo "            \\"analytics-service:8081\\"" >> prometheus/prometheus.yml
                                    echo "          ]" >> prometheus/prometheus.yml
                                else
                                    echo "✅ prometheus.yml уже существует"
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