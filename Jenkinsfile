#!groovy​

pipeline {
    agent any

    stages {
        stage('Compile') {
            steps {
                echo "-=- compiling project -=-"
                sh "mvn clean compile -DskipTests=true"
            }
        }

        stage('Unit tests') {
            steps {
                echo "-=- execute unit tests -=-"
                sh "mvn test"
            }
        }

        stage('Mutation tests') {
            steps {
                echo "-=- execute mutation tests -=-"
                sh "mvn org.pitest:pitest-maven:mutationCoverage"
            }
        }

        stage('Package') {
            steps {
                echo "-=- packaging project -=-"
                sh "mvn package -DskipTests=true"
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

        stage('Code inspection & quality gate') {
            steps {
                echo "-=- run code inspection & quality gate -=-"
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
