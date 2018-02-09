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
                sh "mvn docker:build"
            }
        }

        stage('Deploy to Docker') {
            steps {
                echo "-=- deploy service to Docker Swarm -=-"
                #sh "docker service create -p 8888:8888 --name configservice --network microdemonet deors/deors-demos-microservices-configservice:latest"
                sh "docker service update --container-label-add update_cause="CI-trigger" --update-delay 30s --image deors/deors-demos-microservices-configservice:latest configservice"
            }
        }

        stage('Integration tests') {
            steps {
                echo "-=- execute integration tests -=-"
                sh "mvn integration-test -DskipTests=true -Dtest.targetUrl=http://127.0.0.1:8888"
            }
        }

        stage('Code inspection') {
            steps {
                echo "-=- run code inspection -=-"
                sh "mvn sonar:sonar"
            }
        }

        stage('Push Docker image') {
            steps {
                echo "-=- push Docker image -=-"
                sh "mvn docker:push"
            }
        }
    }
}
