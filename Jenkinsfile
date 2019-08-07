pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                echo 'Building..'
                sh './scripts/build.sh'
            }
        }
        stage('Deploy') {
            steps {
                echo 'Deploying....'
                sh './scripts/deploy.sh'
        }
    }
}