pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                // Run Maven inside a Docker container manually
                sh '''
                    docker run --rm -v $HOME/.m2:/root/.m2 -v $PWD:/app -w /app maven:3.9.9-eclipse-temurin-21 \
                    mvn clean install -DskipTests
                '''
            }
        }
    }
}
