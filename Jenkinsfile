def appName = 'rst-cadastro'
def groupName = 'sesi-viva'

def artifactName = ".ear"


pipeline {
  agent {
    kubernetes {
      label "${appName}-${UUID.randomUUID().toString()}"
      defaultContainer 'jnlp'
      yaml """
      apiVersion: v1
      kind: Pod
      metadata:
      labels:
        component: ci
      spec:
        # Use service account that can deploy to all namespaces
        serviceAccountName: jenkins
        containers:
        - name: maven
          image: gcr.io/cloud-builders/mvn
          command:
          - cat
          tty: true 
      """
}
  }
  options { buildDiscarder(logRotator(numToKeepStr: '1000')) }
  stages {
    stage ('Inicializar') {
      steps {
        script {
          committerEmail = sh (
              script: 'git --no-pager show -s --format=\'%ae\'',
              returnStdout: true
          ).trim()
        }
      }
    }
     stage ('Construir projeto') {
           steps { 
                container('maven') {
                  script {
                      // Baixando dependencias e contruindo projeto
                      withCredentials([[$class:'FileBinding', credentialsId: 'settings-xml', variable: 'SETTINGS']]) {                   
                        dir('rst/rst-app'){
                          sh "mvn clean package -s $SETTINGS -DskipTests"
                        }
                      }
                      // Movendo .jar
                      sh "mv rst/rst-app/target/*.ear ."
                    }
                  }
                }
            }    
  }
  post {
        //always {
        //    script{
        //      def files = findFiles(glob: '**/JUnit*.xml')
        //      for (file in files){
        //        junit "$file"
        //      }
        //    }
        //}
        success {
            archiveArtifacts artifacts: "${artifactName}", fingerprint: true
            rocketSend attachments: [[$class: 'MessageAttachment', text: "Autor do commit: ${committerEmail}\n Build: ${currentBuild.absoluteUrl}\n Artefato: ${currentBuild.absoluteUrl}artifact/${artifactName}", color: 'green']], avatar: 'https://jenkins.io/images/logos/actor/actor.png',
            message: "Pipeline ${currentBuild.fullDisplayName} finalizado com sucesso.", rawMessage: true, channel:"${groupName}"
        }
        failure {
            rocketSend attachments: [[$class: 'MessageAttachment', text: "Autor do commit: ${committerEmail}\n Build: ${currentBuild.absoluteUrl}", color: 'red']],  avatar: 'https://jenkins.io/images/logos/fire/fire.png', 
            message: "Pipeline ${currentBuild.fullDisplayName} finalizado com erro.", rawMessage: true, channel:"${groupName}"
        }       
  }
}
