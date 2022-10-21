def flexcubeDeploy(String jobName) {
    pipelineJob('FlexCube/' + jobName) {
        definition {
            cpsScm {
                scm {
                    git {
                        remote {
                            url('https://github.com/abhi09rawat/seedjobs.git')
                            
                        }
                        branches('main')
                        scriptPath('jenkinsfiles/Flexcube/' + jobName + '.groovy')
                        extensions {}  // required as otherwise it may try to tag the repo, which you may not want
                    }
                }
            }
        }
    }
}


flexcubeDeploy('Infra')
