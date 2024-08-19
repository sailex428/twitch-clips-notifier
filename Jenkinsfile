pipeline {
    agent { label "agent1" }

    environment {
        VERSION = '1.0.0'
        GITHUB_CREDENTIALS_ID = '96096c2a-dbfe-4652-93ac-61b172ccf130'
        DOCKER_CREDENTIALS_ID = 'bd21983e-60e8-4f97-a7f6-3ee67d269a46'
        GITHUB_USERNAME = 'sailex428'
        GITHUB_REPO = "${env.GITHUB_USERNAME}/twitch-clips-notifier"
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
                    withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: '${env.DOCKER_CREDENTIALS_ID}', usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD']]) {
                        sh 'docker build -t twitch-clips-notifier:${env.VERSION} .'
                        sh 'docker tag twitch-clips-notifier:${env.VERSION} DOCKER_USERNAME/twitch-clips-notifier:${env.VERSION}'
                        sh 'docker login -u $DOCKER_USERNAME -p $DOCKER_PASSWORD'
                        sh 'docker push docker push DOCKER_USERNAME/twitch-clips-notifier:${env.VERSION}'
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
