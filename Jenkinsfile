pipeline {

    agent any

    tools {
        maven 'Maven'
    }

    environment {
        JMETER_HOME = "C:\\Users\\Dayton\\Documents\\apache-jmeter"
        PATH = "${JMETER_HOME}\\bin;${PATH}"
        STAGING_PORT = "8081"
        PROD_PORT = "8082"
    }

    stages {

        /* ---------------------------
           BUILD & PACKAGE
        ---------------------------- */

        stage('Build JAR') {
            steps {
                echo "Building JAR with Maven..."
                bat "mvn clean package -DskipTests"
            }
        }

        /* ---------------------------
           BUILD DOCKER IMAGE
        ---------------------------- */

        stage('Build Docker Image') {
            steps {
                echo "Building Docker image atm-app:latest..."
                bat "docker build -t atm-app:latest ."
            }
        }

        /* ---------------------------
           DEPLOY TO STAGING
        ---------------------------- */

        stage('Deploy to Staging') {
            steps {
                echo "Deploying ATM app to staging environment on port 8081..."

                bat """
                    docker stop atm-staging || true
                    docker rm atm-staging || true
                    docker run -d -p ${STAGING_PORT}:8080 --name atm-staging ^
                        -e DD_ENV=staging ^
                        atm-app:latest
                """
            }
        }

        /* ---------------------------
           PERFORMANCE TEST (JMETER)
        ---------------------------- */

        stage('Performance Test') {
            steps {
                echo "Running JMeter continuous load test..."

                bat """
                    jmeter -n ^
                        -t jmeter/ATM_Load_Test.jmx ^
                        -l jmeter/results/results.jtl ^
                        -j jmeter/results/jmeter.log
                """
            }

            post {
                always {
                    echo "Archiving JMeter results..."
                    archiveArtifacts artifacts: 'jmeter/results/*', allowEmptyArchive: true
                }
                unsuccessful {
                    echo "Performance test detected failures — marking build UNSTABLE."
                    unstable("JMeter thresholds not met")
                }
            }
        }

        /* ---------------------------
           APPROVAL BEFORE PRODUCTION
        ---------------------------- */

        stage('Approval to Deploy to Production') {
            when { not { buildingTag() } }
            steps {
                script {
                    timeout(time: 5, unit: 'MINUTES') {
                        input message: "Promote this build to PRODUCTION?"
                    }
                }
            }
        }

        /* ---------------------------
           DEPLOY TO PRODUCTION
        ---------------------------- */

        stage('Deploy to Production') {
            steps {
                echo "Deploying ATM app to PRODUCTION environment on port 8082..."

                bat """
                    docker stop atm-prod || true
                    docker rm atm-prod || true
                    docker run -d -p ${PROD_PORT}:8080 --name atm-prod ^
                        -e DD_ENV=production ^
                        atm-app:latest
                """
            }
        }
    }

    /* ---------------------------
       PIPELINE RESULT HANDLING
    ---------------------------- */

    post {
        success {
            echo "Pipeline complete — Staging and Production deployment successful!"
        }
        unstable {
            echo "Pipeline completed but performance tests indicate potential issues."
        }
        failure {
            echo "Pipeline failed — check logs and JMeter output."
        }
    }
}
