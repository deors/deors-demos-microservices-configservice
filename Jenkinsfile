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
				sh "mvn org.jacoco:jacoco-maven-plugin:prepare-agent test"
			}
		}

		stage('Build Docker image') {
			steps {
				echo "-=- build Docker image -=-"
				sh '''
					eval \$(docker-machine env --shell bash docker-swarm-manager-1)
				    env | grep DOCKER
				    mvn docker:build -DpushImage -DskipTests=true
				'''
			}
		}

		stage('Deploy to Docker') {
			steps {
				echo "-=- deploy service to Docker -=-"
				sh "docker run -p 8888:8888 --name config-service -t deors/deors.demos.microservices.configservice:latest"
			}
		}
	}
}
