pipeline {
    agent any
    stages {
        stage('Unit Tests') {
            steps {
                echo 'Running unit tests...'
                sh '''
                    cd games-swap-service && ./mvnw test -B
                    cd ../analytics-service && ./mvnw test -B
                    cd ../audit-service && ./mvnw test -B
                    cd ../simple-notification-service && ./mvnw test -B
                '''
            }
        }
    }
}