pipeline {
    agent any
    options {
        ansiColor('xterm')
    }
    environment {
        // git相关
        GIT_URL = "git@github.com:liye111111/shopify-draw-server.git "
        // 镜像仓库相关
        DOCKER_REGISTER = "10.10.11.140:5000"
        IMAGE_NAME = "shopify-draw-server"
        DOCKER_FILE = "Dockerfile_dev"
        // 应用配置
        APP_NAME = "shopify-draw-server"
        SERVER_MODULE = "server/server-draw"
        BRANCH = "main"
        // k8s部署相关
        NAMESPACE = "app"
        DEPLOYMENT_NAME = "$APP_NAME"
        TIMEOUT = "120s"

    }

    stages {
        stage('Debug Workspace') {
            steps {
                sh 'echo $HOME;ls -la;mvn --version'
            }
        }
        stage('Pull Project') {
            steps {
                dir("${APP_NAME}") {
                    sh '''
                    export https_proxy=https://localhost:8899;export http_proxy=http://localhost:8899;export all_proxy=socks5://localhost:8899
                    if [ -d .git ];then git pull; git checkout -f $BRANCH; else git clone --branch ${BRANCH} ${GIT_URL} . ;fi
                    git rev-parse --short HEAD
                    '''
                }
            }
        }
        stage('Build boms-3rd') {
            steps {
                dir("${APP_NAME}") {
                    sh '''
                    cd module/boms-3rd
                    mvn clean install -DskipTests -T 1C                    
                    '''
                }
            }
        }
        stage('Build boms') {
            steps {
                dir("${APP_NAME}") {
                    sh '''                    
                    cd module/boms
                    mvn clean install -DskipTests -T 1C
                    '''
                }
            }
        }

        stage('Build Project') {
            steps {
                dir("${APP_NAME}") {
                    sh '''
                    mvn clean install -DskipTests -T 1C
                    '''
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                dir("${APP_NAME}") {
                    script {
                        def gitHash = sh(script: 'git rev-parse --short HEAD', returnStdout: true).trim()
                        def timestamp = sh(script: 'date +%s', returnStdout: true).trim()
                        env.IMAGE_TAG = "${gitHash}-${timestamp}"
                    }
                }
                sh '''
                rm -rf .build
                mkdir -p .build                
                cp -rf ${APP_NAME}/.dev/* .build/
                cp -rf ${APP_NAME}/${SERVER_MODULE}/target .build/
                cd .build
                docker build -f ${DOCKER_FILE} -t ${DOCKER_REGISTER}/${IMAGE_NAME}:${IMAGE_TAG} .               
                '''
            }
        }
        stage('Push Docker Image') {
            steps {
                sh '''
                docker push ${DOCKER_REGISTER}/${IMAGE_NAME}:${IMAGE_TAG}                
                '''
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                dir(".build") {
                    sh '''
                    mkdir -p manifests
                    sed -e "s@{{IMAGE}}@${DOCKER_REGISTER}/${IMAGE_NAME}:${IMAGE_TAG}@g"  -e "s@{{APP_NAME}}@${APP_NAME}@g" -e "s@{{NAMESPACE}}@${NAMESPACE}@g"  deploy_dev.yaml >  deploy_update.yaml; cat deploy_update.yaml
                    kubectl apply -n ${NAMESPACE} -f deploy_update.yaml --validate=false
                    kubectl rollout status deployments/${DEPLOYMENT_NAME} -n ${NAMESPACE} --timeout=${TIMEOUT}
                    '''
                }

            }
        }
    }
}