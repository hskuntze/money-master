pipeline {
    agent any
    tools{
        maven "mvn"
    }
    environment{
        DOCKER_CREDS= credentials('dockerhub')
    }

    stages {
        stage('Initialize') {
            steps {
                sh '''
                    echo "PATH=${PATH}"
                    echo "M2_HOME=${M2_HOME}"
                '''
            }
        }
        stage('Git Checkout') {
            steps {
                git(
                    url:'https://github.com/hskuntze/money-master-backend.git',
                    branch:'master'
                    )
                script{
                    TAG_VERSION = sh(script: 'git rev-parse --short HEAD', returnStdout:true).trim()
                }
            }
        }
        stage('Build') {
            steps{
                sh "mvn -DskipTests clean package"
            
            }
        }
        stage('Create Image') {
            steps{
                echo "IMAGE VERSION: ${TAG_VERSION}"
                sh "docker build . -f Dockerfile -t kuntze/money-master-api-prod:${TAG_VERSION}"
                sh "docker tag kuntze/money-master-api-prod:${TAG_VERSION} kuntze/money-master-api-prod:latest"
            }
        }
        stage('Push Image') {
            steps{
                sh "docker login --username=$DOCKER_CREDS_USR --password=$DOCKER_CREDS_PSW"
                sh "docker push kuntze/money-master-api-prod:${TAG_VERSION}"
                sh "docker push kuntze/money-master-api-prod:latest "
            }
        }
        stage('Clean Images') {
            steps{
                sh "docker image rm kuntze/money-master-api-prod:${TAG_VERSION}"
                sh "docker image rm kuntze/money-master-api-prod:latest"
            }
        }
    }
}