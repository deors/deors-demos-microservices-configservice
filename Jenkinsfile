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
				sh "mvn clean org.jacoco:jacoco-maven-plugin:prepare-agent test"
	  			step([$class: 'ArtifactArchiver', artifacts: '**/target/*.jar', excludes: null, fingerprint: true, onlyIfSuccessful: true])
	  			try {
	    			step([$class: 'JUnitResultArchiver', testResults: '**/target/surefire-reports/TEST-*.xml'])
	  			} catch (err) {
	    			echo "No unit tests result were found: ${err}"
	  			}
			}
		}

		stage('Build Docker image') {
			steps {
				echo "-=- build Docker image -=-"
				sh "mvn package docker:build -DpushImage"
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
