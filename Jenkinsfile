pipeline {
  agent any

  tools {
    jdk 'JDK17'
    maven 'Maven3.9'
  }

  environment {
    DOCKER_IMAGE = "atm-app"
    DOCKER_TAG = "latest"
    // If you want to push, set DOCKERHUB_ACCOUNT:
    DOCKERHUB_ACCOUNT = "daytonhannaforddev"
  }

  stages {

    stage('Checkout Code') {
      steps {
        git url: 'https://github.com/daytonhannaforddev/FinalProject.git', branch: 'main'
      }
    }

    stage('Build with Maven') {
      steps {
        sh 'mvn clean package -DskipTests'
      }
    }

    stage('Build Docker Image') {
      steps {
        sh "docker build -t ${DOCKERHUB_ACCOUNT}/${DOCKER_IMAGE}:${DOCKER_TAG} ."
      }
    }

    stage('Run Docker Container') {
      steps {
        // Stop & remove old container if exists
        sh """
          docker stop atm-container || true
          docker rm atm-container || true
        """
        // Run the new container mapping port 8081 (or 8080 if you kept original)
        sh "docker run -d -p 8081:8081 --name atm-container ${DOCKERHUB_ACCOUNT}/${DOCKER_IMAGE}:${DOCKER_TAG}"
      }
    }

    stage('Smoke Test') {
      steps {
        // Test the /atm/balance endpoint quickly
        sh '''
          sleep 5
          curl -I http://localhost:8081/atm/balance
        '''
      }
    }

    post {
      success {
        echo "✅ Pipeline succeeded!"
      }
      failure {
        echo "❌ Pipeline failed!"
      }
    }
  }
}
