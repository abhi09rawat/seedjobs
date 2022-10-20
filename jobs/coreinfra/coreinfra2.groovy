def coreInfraDeploy(String jobName) {
    pipelineJob('COREINFRA-2/Deploy-2/' + jobName) {
        definition {
            cpsScm {
                scm {
                    git {
                        remote {
                            url('https://github.com/abhi09rawat/seedjobs.git')
                            
                        }
                        branches('main')
                        scriptPath('jenkinsfiles/coreinfra/' + jobName + '.groovy')
                        extensions {}  // required as otherwise it may try to tag the repo, which you may not want
                    }
                }
            }
        }
    }
}


coreInfraDeploy('Infra')
