import org.jenkinsci.plugins.scriptsecurity.scripts.*

accountMap = [
  "aws-classic-dev":"696758188485",
  "aws-classic-tst":"047040744730",
  "aws-classic-stg":"890843259060",
  "aws-dataplatform-dev":"720028829347",
  "aws-dataplatform-tst":"459046291969",
  "aws-dataplatform-stg":"802416586314",
  "aws-devportal-dev":"038177840223",
  "aws-devportal-tst":"399721441408",
  "aws-devportal-stg":"843930438638",
  "aws-main-dev":"218242235676",
  "aws-main-tst":"929835347540",
  "aws-main-stg":"234432223397",
  "aws-psd2fallback-tst":"358834821711",
  "aws-psd2fallback-stg":"812612628310",
  "aws-tcs-dev":"716550419445",
  "aws-tcs-tst":"239957912596",
  "aws-tcs-stg":"445085056923",
]

def forceSchedulerDeploy(String jobName, String jenkinsfileScript) {
    pipelineJob('COREINFRA/Deploy/payments/prd/' + jobName) {
				parameters {
						activeChoiceParam('accountName') {
								description('Allows user choose from multiple choices')
								filterable()
								choiceType('CHECKBOX')
								groovyScript {
										script('return ["'+accountMap.keySet().sort().join('","')+'"]')
										fallbackScript('"fallback choice"')
								}
						}

						activeChoiceParam('action') {
								delegate.description('Choose force state or clear current. NOTE: To remove forcing state and go back to schedule - choose "CLEAR" action, NOT "stopped" or "running".')
								delegate.choiceType('RADIO')
								delegate.groovyScript {
										script('return ["running","stopped","clear"]')
								}
						}
						activeChoiceReactiveParam('accountId') {
								description('Allows user choose from multiple choices')
								choiceType('CHECKBOX')
								groovyScript {
										script("""
                    def accountMap = ${accountMap.inspect()}
                    def tmpList = []

                    for(val in accountName.tokenize(','))
                      tmpList += accountMap[val]+':selected:disabled'

                    return tmpList
                    """)
										fallbackScript('"Failed to find relation for selected Account Name"')
								}
                referencedParameter('accountName')
						}
				}
        definition {
            cpsScm {
                scm {
                    git {
                        remote {
                            url('https://git.onelum.host/lds/jenkinsfiles.git')
                            credentials('gitlab-credentials')
                        }
                        branches('master')
                        scriptPath('jenkinsfiles/coreinfra/ops/shared-services/prd/'  + jenkinsfileScript)
                        extensions {}  // required as otherwise it may try to tag the repo, which you may not want
                    }
                }
            }
        }
    }
}


forceSchedulerDeploy('force_instance_scheduling_on_account','force_scheduler.groovy')
