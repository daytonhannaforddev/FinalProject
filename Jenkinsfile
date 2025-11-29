pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/daytonhannaforddev/FinalProject.git'
            }
        }

        stage('Build JAR') {
            steps {
                sh 'mvn -B clean package'
            }
        }

        stage('Build Docker Image') {
            steps {
                sh 'docker build -t atm-app:latest .'
            }
        }

        stage('Run Docker Container') {
            steps {
                sh '''
                    docker rm -f atm-container || true
                    docker run -d -p 8081:8081 --name atm-container atm-app:latest
                '''
            }
        }
    }

    post {
        success {
            echo "Pipeline complete!"
        }
        failure {
            echo "Pipeline failed!"
        }
    }
}
