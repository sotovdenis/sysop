pipeline {
    agent any

    tools {
        maven 'maven-3.9.9'
        jdk 'jdk-21'
    }

    stages {
        stage('Debug: Check JDK & Maven') {
            steps {
                sh 'echo "Java version:"'
                sh 'java -version'
                sh 'echo "JAVA_HOME: $JAVA_HOME"'
                sh 'echo "Maven version:"'
                sh 'mvn -v'
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean install -DskipTests'
            }
        }
    }
}
