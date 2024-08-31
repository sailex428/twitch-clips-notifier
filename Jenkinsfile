pipeline {
    agent {
        label "agent1"
        docker {
            args '-v /var/run/docker.sock:/var/run/docker.sock'
        }
    }

    environment {
        VERSION = 'v1.0.0'
        GITHUB_CREDENTIALS_ID = '96096c2a-dbfe-4652-93ac-61b172ccf130'
        DOCKER_CREDENTIALS_ID = 'bd21983e-60e8-4f97-a7f6-3ee67d269a46'
        SSH_SERVER_CREDENTIALS_ID = ''
        GITHUB_USERNAME = 'sailex428'
        GITHUB_REPO_NAME = 'twitch-clips-notifier'
        GITHUB_REPO = "${env.GITHUB_USERNAME}/${env.GITHUB_REPO_NAME}"
        DOCKER_IMAGE_NAME = "${env.GITHUB_REPO_NAME}:${env.VERSION}"
    }

    stages {

        stage("Checkout") {
            steps {
                git branch: "main",
                    url: "https://github.com/${env.GITHUB_REPO}.git",
                    credentialsId: "${env.GITHUB_CREDENTIALS_ID}"
            }
        }

        stage('Build Jar') {
            steps {
                sh './gradlew build'
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: "${DOCKER_CREDENTIALS_ID}", usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD']]) {
                        sh "docker build -t ${env.DOCKER_IMAGE_NAME} ."
                        sh "docker tag ${env.DOCKER_IMAGE_NAME} ${DOCKER_USERNAME}/${env.DOCKER_IMAGE_NAME}"
                        sh "docker login -u ${DOCKER_USERNAME} -p ${DOCKER_PASSWORD}"
                        sh "docker push ${DOCKER_USERNAME}/${env.DOCKER_IMAGE_NAME}"
                    }
                }
            }
        }

        stage('Deploy Image on server') {
            steps {
                script {
                    sshagent([[credentialsId: "${SSH_SERVER_CREDENTIALS_ID}"]]) {
                        sh "docker rm ${env.GITHUB_REPO_NAME}"
                        sh "docker pull ${env.DOCKER_IMAGE_NAME}"
                        sh "docker run ${env.DOCKER_IMAGE_NAME}"
                    }
                }
            }
        }

    }

    post {
        success {
            echo 'Built and published docker image successfully!'
        }
        failure {
            echo 'Failed to build and publish docker image!'
        }
    }

}
