#!groovy​

pipeline {
    agent any

    stages {
        stage('Compile & package') {
            steps {
                echo "-=- building project -=-"
                sh "mvn clean package -DskipTests=true"
            }
        }

        stage('Unit tests') {
            steps {
                echo "-=- execute unit tests -=-"
                sh "mvn test"
            }
        }

        stage('Muration tests') {
            steps {
                echo "-=- execute mutation tests -=-"
                sh "mvn org.pitest:pitest-maven:mutationCoverage"
            }
        }

        stage('Build Docker image') {
            steps {
                echo "-=- build Docker image -=-"
                sh '''
                    eval \$(docker-machine env --shell bash docker-swarm-manager-1)
                    mvn docker:build -DpushImage -DskipTests=true
                '''
            }
        }

        stage('Deploy to Docker') {
            steps {
                echo "-=- deploy service to Docker Swarm -=-"
                sh '''
                    eval \$(docker-machine env --shell bash docker-swarm-manager-1)
                    docker service update --container-label-add update_cause="CI-trigger" --update-delay 30s --image deors/deors.demos.microservices.configservice:latest config-service
                '''
            }
        }

        stage('Integration tests') {
            steps {
                echo "-=- execute integration tests -=-"
                sh "mvn integration-test -Dtest.targetUrl=config-service"
            }
        }

        stage('Code inspection') {
            steps {
                echo "-=- run code inspection -=-"
                sh "mvn sonar:sonar"
            }
        }
    }
}
