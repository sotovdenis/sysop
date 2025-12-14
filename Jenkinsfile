pipeline {
    agent any

    tools {
        maven 'maven-3.9.9'
        jdk 'jdk21'
    }

    stages {
        stage('Build') {
            steps {
                sh 'mvn clean install -DskipTests'
            }
        }
    }
}
