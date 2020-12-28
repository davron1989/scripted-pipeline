properties([
    parameters([
        string(defaultValue: '', description: 'Input node IP', name: 'SSHNODE', trim: true)
    ])
])
node {
    withCredentials([sshUserPrivateKey(credentialsId: 'jenkins-master', keyFileVariable: 'SSHKEY', passphraseVariable: '', usernameVariable: 'SSHUSERNAME')]){ 
        stage("Initialize"){
            sh "ssh -o StrictHostKeyChecking=no -i $SSHKEY $SSHUSERNAME@${params.SSHNODE} yum install epel-release -y "
        }
        stage("Installin Python3"){
            sh "ssh -o StrictHostKeyChecking=no -i $SSHKEY $SSHUSERNAME@${params.SSHNODE} yum install -y python3 -y "
        }
        stage("Installin Pip"){
            sh "ssh -o StrictHostKeyChecking=no -i $SSHKEY $SSHUSERNAME@${params.SSHNODE} yum install python-pip -y "
        }
        stage("Installing git"){
            sh "ssh -o StrictHostKeyChecking=no -i $SSHKEY $SSHUSERNAME@${params.SSHNODE} yum install git -y "
        }
        stage("cloning repo"){
            sh "ssh -o StrictHostKeyChecking=no -i $SSHKEY $SSHUSERNAME@${params.SSHNODE} git clone https://github.com/anfederico/Flaskex "
        }
        stage("changing directory"){
            sh "ssh -o StrictHostKeyChecking=no -i $SSHKEY $SSHUSERNAME@${params.SSHNODE} pwd"
        }
        stage("changing directory"){
            sh "ssh -o StrictHostKeyChecking=no -i $SSHKEY $SSHUSERNAME@${params.SSHNODE} ls > test.txt"
        }
        stage("Installing requirements"){
            sh "ssh -o StrictHostKeyChecking=no -i $SSHKEY $SSHUSERNAME@${params.SSHNODE} pip install -r requirements.txt"
        }
        stage("Installing application"){
            sh "ssh -o StrictHostKeyChecking=no -i $SSHKEY $SSHUSERNAME@${params.SSHNODE} python app.py "
        }
    }    
}