pipeline {
    agent any

    tools {
        maven 'maven_3_9_9'
        jdk 'jdk_17'
    }

    stages {
        stage('Build Maven') {
            steps {
                checkout scmGit(branches: [[name: "*/main"]],
                    extensions: [],
                    userRemoteConfigs: [[url: 'https://github.com/martinex453/TingesoBackend-martin-gamboa.git']])
                bat 'dir'
                bat 'mvn clean package'
            }
        }

        
        stage('Backend Unit Tests') {
            steps {
                bat 'mvn test'
            }
        }
        
        stage('Build Docker Images') {
            steps {
                script {
                    bat 'docker build -t martinex453/backend-martin-gamboa:latest .'
                }
            }
        }
        
        stage('Push Docker Images to Docker Hub') {
            steps {
                script {
                    withCredentials([string(credentialsId: 'mgvkC', variable: 'mgvk')]) {
                        bat 'docker login -u martinex453 -p %mgvk%'
                    }
                    bat 'docker push martinex453/backend-martin-gamboa:latest'
                }
            }
        }
    }
}
