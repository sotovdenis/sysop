pipeline {
    stages {
        stage('Build') {
            steps {
                sh 'mvn -version'
                sh 'java -version'
                sh 'mvn clean install -DskipTests'
            }
        }
    }
}
