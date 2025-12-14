pipeline {
    agent {
        docker {
            image 'eclipse-temurin:21-jdk'
            args '-v /var/maven/.m2:/root/.m2'
        }
    }

    stages {
        stage('Build') {
            steps {
                sh 'java -version'
                sh 'mvn -version'
                sh 'mvn clean install -DskipTests'
            }
        }
    }
}
