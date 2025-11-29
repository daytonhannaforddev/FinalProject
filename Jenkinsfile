pipeline {
    agent any

    tools {
        maven 'Maven'
    }

    stages {

        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/daytonhannaforddev/FinalProject.git'
            }
        }

        stage('Build JAR') {
            steps {
                bat 'mvn clean package -DskipTests'
            }
        }

        stage('Build Docker Image') {
            steps {
                bat 'docker build -t atm-app:latest .'
            }
        }

        stage('Run Docker Container') {
            steps {
                bat '''
                    docker stop atm-container || true
                    docker rm atm-container || true
                    docker run -d -p 8081:8080 --name atm-container atm-app:latest
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
