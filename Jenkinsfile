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

        stage('Performance Test (JMeter)') {
            steps {
                echo 'Running JMeter load test...'
                // Adjust path if needed — ensure jmeter.bat is installed on the Jenkins machine and in PATH
                bat """
                    jmeter -n -t jmeter/ATM_Load_Test.jmx ^
                           -l jmeter/results/results.jtl ^
                           -j jmeter/results/jmeter.log
                """
            }
            post {
                always {
                    archiveArtifacts artifacts: 'jmeter/results/*.jtl, jmeter/results/*.log', allowEmptyArchive: true
                }
                unsuccessful {
                    echo 'Performance test detected failures — marking build as UNSTABLE.'
                    unstable('Load test failed or thresholds not met')
                }
            }
        }
    }

    post {
        success {
            echo 'Pipeline succeeded — build, deploy, and performance test passed.'
        }
        unstable {
            echo 'Pipeline succeeded but performance tests have issues (unstable build).'
        }
        failure {
            echo 'Pipeline failed — check errors above.'
        }
    }
}
