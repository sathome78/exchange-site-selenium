// Pipeline Utility Steps Plugin required

@Library('jenkins-pipeline-library')_
def generated_label = "selenium-${UUID.randomUUID().toString()}"

def kubeconfigMap = "kube-config-exrates-k8s-name"
def kubeconfigMountPath = "/kubeconfig"
def podMemoryRequests = '1Gi'
def podCPURequests = '500m'

def selenium_browser_count(String count, String active) {
  if("${active}" == "yes") { 
     return "${count}".toInteger()
  } else{
     return 0
  }
}

def wait_browser_connected_to_hub(String browser_ulr, String browser_name) {
  echo "wait_browser_connected_to_hub... (${browser_ulr} ${browser_name})"
  sh "./wait-browser-connected-to-hub ${browser_ulr} ${browser_name}"
}

pipeline {
  agent {
    kubernetes {
      cloud 'kubernetes'
      label generated_label
      defaultContainer 'docker-helm-kubectl'
      yaml selenium(kubeconfigMap, kubeconfigMountPath, podMemoryRequests, podCPURequests)
    }
  }


  parameters {
      string(name: 'TEST_PROFILE',
        defaultValue: "selenium-tests",
        description: 'Specify test profile'
      )

      string(name: 'FIREFOX',
        defaultValue: "no",
        description: 'Run Firefox tests'
      )

      string(name: 'FIREFOX_HEADLESS',
        defaultValue: "true",
        description: 'Use firefox in headless mode'
      )
      
      string(name: 'FIREFOX_DESIRED_CONTAINER_COUNT',
        defaultValue: "10",
        description: 'Firefox selenium workers count'
      )

      string(name: 'FIREFOX_MEM_LIMIT',
        defaultValue: "1000Mi",
        description: 'Firefox workers Memory limit'
      )
      
      string(name: 'FIREFOX_CPU_LIMIT', 
        defaultValue: "0.5",
        description: 'Firefox workers CPU limit'
      )

      string(name: 'FIREFOX_THREAD_COUNT', 
        defaultValue: "1",
        description: 'Firefox thread count'
      )

      string(name: 'CHROME',
        defaultValue: "yes",
        description: 'Run Chrome tests'
      )

      string(name: 'CHROME_HEADLESS',
        defaultValue: "true",
        description: 'Use chrome in headless mode'
      )

      string(name: 'CHROME_DESIRED_CONTAINER_COUNT',
        defaultValue: "10",
        description: 'Chrome selenium workers count'
      )

      string(name: 'CHROME_MEM_LIMIT',
        defaultValue: "1000Mi",
        description: 'Chrome workers Memory limit'
      )

      string(name: 'CHROME_CPU_LIMIT',
        defaultValue: "0.5",
        description: 'Chrome workers CPU limit'
      )

      string(name: 'CHROME_THREAD_COUNT', 
        defaultValue: "1",
        description: 'Chrome thread count'
      )

      string(name: 'HUB_BROWSERTIMEOUT',
        defaultValue: "30",
        description: 'hub gridBrowserTimeout'
      )

      string(name: 'HUB_GRIDTIMEOUT',
        defaultValue: "60",
        description: 'hub gridTimeout'
      )

      string(name: 'BRANCH',
        defaultValue: env.BRANCH_NAME,
        description: 'Specify branch name'
      )

    }

  environment {
    PIPELINE_BRANCH = "${params.BRANCH}"
    SERVICE_NAME = "$generated_label"

    KUBECONFIG = "$kubeconfigMountPath/$kubeconfigMap"
    KUBERNETES_NAMESPACE = "jenkins"

    FIREFOX = "${params.FIREFOX}"
    CHROME = "${params.CHROME}"

    CHROME_CPU_LIMIT = "${params.CHROME_CPU_LIMIT}"
    CHROME_MEM_LIMIT = "${params.CHROME_MEM_LIMIT}"

    FIREFOX_CPU_LIMIT = "${params.FIREFOX_CPU_LIMIT}"
    FIREFOX_MEM_LIMIT = "${params.FIREFOX_MEM_LIMIT}"

  }

  stages{

    stage('kubectl config and test connection') {
      steps {
        container('docker-helm-kubectl') {
          ansiColor('xterm') {
              echo "running kubectl connection test"
              echo "cat $KUBECONFIG > ~/.kube/config"
              sh "kubectl get pods --namespace $KUBERNETES_NAMESPACE"   
          }
        }
      }
    }

    stage('checkout helm repo') {
      steps {
          ansiColor('xterm') {
              dir("helm") {
                checkout([
                  $class: 'GitSCM',
                  branches: [[name: "master"]],
                  doGenerateSubmoduleConfigurations: false,
                  extensions: [
                     [$class: 'CloneOption',
                          depth: 1,
                          shallow: true
                     ]
                  ],
                  submoduleCfg: [],
                  userRemoteConfigs: [[credentialsId: 'jenkins-pipeline-ssh-key', url: 'ssh://git@bitbucket.to-the-moon-team-of-world-largest-exchange.com:8979/ops/exrates-ops-tools.git']]
                ])

                script {
                        env.HELM_REPO_PATH=sh(returnStdout: true, script: "pwd").trim()
                        env.CHROME_DESIRED_CONTAINER_COUNT_IF_ACTIVE = selenium_browser_count("${params.CHROME_DESIRED_CONTAINER_COUNT}", "$CHROME")
                        env.FIREFOX_DESIRED_CONTAINER_COUNT_IF_ACTIVE = selenium_browser_count("${params.FIREFOX_DESIRED_CONTAINER_COUNT}", "$FIREFOX")
                }
              }
          }
      }
    }

    stage('helm') {
        steps {
            ansiColor('xterm') {
                echo "Running helm dry-run deployment"
                sh "cd $HELM_REPO_PATH/k8s.service/helm/selenium && helm init --upgrade --client-only"
                echo "Running helm dry-run deployment"
                sh "cd $HELM_REPO_PATH/k8s.service/helm/selenium && /usr/local/bin/helm upgrade --install --dry-run $SERVICE_NAME --namespace $KUBERNETES_NAMESPACE  --set chrome.replicas=${env.CHROME_DESIRED_CONTAINER_COUNT_IF_ACTIVE} --set firefox.replicas=${env.FIREFOX_DESIRED_CONTAINER_COUNT_IF_ACTIVE} --set hub.service_name=$SERVICE_NAME --set chrome.resources.limits.cpu=$CHROME_CPU_LIMIT --set chrome.resources.limits.memory=$CHROME_MEM_LIMIT --set firefox.resources.limits.cpu=$FIREFOX_CPU_LIMIT --set firefox.resources.limits.memory=$FIREFOX_MEM_LIMIT --set gridBrowserTimeout=${params.HUB_BROWSERTIMEOUT} --set gridTimeout=${params.HUB_GRIDTIMEOUT} . "
                echo "Running helm run deployment"
                sh "cd $HELM_REPO_PATH/k8s.service/helm/selenium && /usr/local/bin/helm upgrade --install $SERVICE_NAME --namespace $KUBERNETES_NAMESPACE --set chrome.replicas=${env.CHROME_DESIRED_CONTAINER_COUNT_IF_ACTIVE} --set firefox.replicas=${env.FIREFOX_DESIRED_CONTAINER_COUNT_IF_ACTIVE} --set hub.service_name=$SERVICE_NAME --set chrome.resources.limits.cpu=$CHROME_CPU_LIMIT --set chrome.resources.limits.memory=$CHROME_MEM_LIMIT --set firefox.resources.limits.cpu=$FIREFOX_CPU_LIMIT --set firefox.resources.limits.memory=$FIREFOX_MEM_LIMIT --set gridBrowserTimeout=${params.HUB_BROWSERTIMEOUT} --set gridTimeout=${params.HUB_GRIDTIMEOUT} . --wait"
            }
        }
    }

    stage('selenium') {
            parallel {
                stage('maven-chrome') {
                    when { expression { CHROME == 'yes' } }
                    steps {
                      container('maven-chrome') {
                        ansiColor('xterm') {
                          wait_browser_connected_to_hub('http://$SERVICE_NAME:4444', 'chrome')
                          sh 'export MAVEN_OPTS="-XX:MaxPermSize=512m"; mvn -B clean verify -P${TEST_PROFILE} -Dthreads=${CHROME_THREADS_COUNT} -Dremote=true -DseleniumGridURL=http://$SERVICE_NAME:4444/wd/hub -Dsleep=0 -f pom.xml -Dbrowser=chrome -Dheadless=${CHROME_HEADLESS}'

                        }
                      }
                    }
                }
                stage('maven-firefox') {
                    when { expression { FIREFOX == 'yes' } }
                    steps {
                      container('maven-firefox') {
                        ansiColor('xterm') {
                          wait_browser_connected_to_hub('http://$SERVICE_NAME:4444', 'firefox')
                          sh 'export MAVEN_OPTS="-XX:MaxPermSize=512m"; mvn -B clean verify -P${TEST_PROFILE} -Dthreads=${FIREFOX_THREAD_COUNT} -Dremote=true -DseleniumGridURL=http://$SERVICE_NAME:4444/wd/hub -Dsleep=0 -f pom.xml -Dbrowser=firefox -Dheadless=${FIREFOX_HEADLESS}'
                        }
                      }
                    }
                }
            }
        }

  }

  post {

      success {
          echo "SUCCESS"
      }
      unstable {
          echo "UNSTABLE"
      }
      failure {
          echo "FAILURE"
      }
      always {

        sh "helm delete --purge $SERVICE_NAME"

        archiveArtifacts artifacts: 'target/screenshots/*.png', fingerprint: true, allowEmptyArchive: true
        archiveArtifacts artifacts: 'target/failsafe-reports/*.*', fingerprint: true, allowEmptyArchive: true
        step([$class: 'Publisher', reportFilenamePattern: 'target/failsafe-reports/testng-results.xml'])
      }
    }
}
