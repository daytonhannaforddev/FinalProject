pipeline {
    agent any

    tools {
        maven 'Maven'
    }

    stages {

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

        stage('Performance Test') {
            steps {
                echo 'Running JMeter load test...'

                bat """
                    jmeter -n -t jmeter/ATM_Load_Test.jmx ^
                           -l jmeter/results/results.jtl ^
                           -j jmeter/results/jmeter.log
                """
            }

            post {
                always {
                    archiveArtifacts artifacts: 'jmeter/results/*', allowEmptyArchive: true
                }
                unsuccessful {
                    echo 'Performance test failed! Build marked as UNSTABLE.'
                    unstable('Load test thresholds not met')
                }
            }
        }
    }

    post {
        success {
            echo "Pipeline complete!"
        }
        unstable {
            echo "Pipeline completed but tests were unstable"
        }
        failure {
            echo "Pipeline failed!"
        }
    }
}
