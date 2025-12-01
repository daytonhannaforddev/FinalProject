pipeline {
    agent any

    tools {
        maven 'Maven'
    }

    environment {
        JMETER_HOME = 'C:\\Users\\Dayton\\Documents\\apache-jmeter'
        PATH = "${env.PATH};${env.JMETER_HOME}\\bin""
    }

    stages {

        stage('Build JAR') {
            steps {
                echo "Building Java application..."
                bat 'mvn clean package -DskipTests'
            }
        }

        stage('Build Docker Image') {
            steps {
                echo "Building Docker image..."
                bat 'docker build -t atm-app:latest .'
            }
        }

        stage('Deploy to Production') {
            steps {
                echo "Deploying ATM app to production..."

                bat """
                    docker stop atm-container 2>nul || exit /b 0
                    docker rm atm-container 2>nul || exit /b 0

                    docker run -d -p 8081:8080 --name atm-container ^
                        -e DD_ENV=production ^
                        atm-app:latest
                """
            }
        }

        stage('Performance Test') {
            steps {
                echo "Running JMeter load test..."

                bat """
                    jmeter -n -t jmeter/ATM_Load_Test.jmx ^
                           -l jmeter/results/results.jtl ^
                           -j jmeter/results/jmeter.log
                           -e -o jmeter/dashboard
                """
            }

            post {
                always {
                    archiveArtifacts artifacts: 'jmeter/results/*', allowEmptyArchive: true
                    archiveArtifacts artifacts: 'jmeter/dashboard/**', allowEmptyArchive: true
                }
                unsuccessful {
                    echo "Performance test detected failures — marking build as UNSTABLE."
                    unstable("Load test thresholds not met")
                }
            }
        }
    }

    post {
        success {
            echo "Pipeline completed successfully."
        }
        unstable {
            echo "Pipeline completed, but tests were UNSTABLE."
        }
        failure {
            echo "Pipeline FAILED — check logs above."
        }
    }
}
