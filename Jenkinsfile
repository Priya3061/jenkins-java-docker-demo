pipeline {
    agent any

    // Lets you choose deployment options when clicking "Build with Parameters"
    parameters {
        choice(name: 'DEPLOY_ENV', choices: ['DEV', 'QA', 'PROD'], description: 'Select target environment')
        booleanParam(name: 'RUN_DOCKER', defaultValue: true, description: 'Build and run Docker image if Docker is installed')
    }

    environment {
        APP_NAME = 'greeting-app'
        IMAGE_TAG = "${env.BUILD_NUMBER}"
    }

    stages {
        stage('Checkout') {
            steps {
                echo "Starting Build #${IMAGE_TAG} for ${APP_NAME}..."
                checkout scm
            }
        }

        stage('Build & Test') {
            steps {
                sh 'mvn clean test'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }

        stage('Package JAR') {
            steps {
                sh 'mvn package -DskipTests'
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            }
        }

        stage('Smoke Test JAR') {
            steps {
                echo "Verifying the packaged JAR runs cleanly for ${params.DEPLOY_ENV}..."
                sh "java -jar target/greeting-app-1.0.0.jar ${params.DEPLOY_ENV}"
            }
        }

        stage('Docker Build & Run') {
            when {
                expression { return params.RUN_DOCKER == true }
            }
            steps {
                script {
                    // Safely check if Docker is available on this Jenkins machine
                    def dockerInstalled = sh(script: 'command -v docker', returnStatus: true) == 0
                    if (dockerInstalled) {
                        sh "docker build -t ${APP_NAME}:${IMAGE_TAG} ."
                        sh "docker run --rm ${APP_NAME}:${IMAGE_TAG} ${params.DEPLOY_ENV}"
                    } else {
                        echo 'Docker is not installed on this server—skipping container step cleanly.'
                    }
                }
            }
        }
    }

    post {
        always {
            echo "Pipeline finished for environment: ${params.DEPLOY_ENV}"
        }
    }
}
