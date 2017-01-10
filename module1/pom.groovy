project {

    modelVersion '4.0.0'
    parent {
        groupId 'org.exampledriven'
        artifactId 'maven-polyglot-example'
        version '1.0-SNAPSHOT'
    }
    artifactId 'module1'


    dependencies {
        dependency {
            groupId 'org.apache.maven.plugin-tools'
            artifactId 'maven-plugin-annotations'
            version 3.4
        }
    }

    properties {
        'greet' 'true'
        'greet2' 'true'
    }

    build {

        $execute(id: 'hello', phase: 'compile') {

            if ("true".equals(System.getProperty("file-test"))) {
                println "File generation is enabled"

                println properties.size()
                println this.getProperties().containsKey('greet')
                println properties
                println properties.get('greet')

                if (getProperties().getOrDefault('greet', false)) {
                    System.out.println 'Hello from groovy'
                }

                def directory = "target/classes/new"
                def dirCreated = new File(directory).mkdir();

                println "Directory was created? " + dirCreated
                def file = new File(directory + "/hello.txt")

                if (!file.exists()) {
                    println "Creating hello.txt"

                    file.createNewFile();
                    file.write("hello from groovy")

                } else {
                    println "hello.txt is already created"

                }
            } else {
                println "File generation is disabled"
            }

        }

    }

    profiles {
        profile {
            activation {
//                activeByDefault 'true'
                property {
                    name 'greet'
                }
            }
            build {
                plugins {
                    plugin {
                        groupId 'com.github.exampledriven'
                        artifactId 'hierarchy-maven-plugin'
                        version '1.4'
                        executions {
                            execution {
                                id 'install'
                                phase 'install'

                                goals 'tree'


                            }
                        }
                    }
                }
            }
        }
        profile {
            id 'javadoc'
            activation {
                property {
                    name 'javadoc'
                }
            }
            build {
                plugins {
                    plugin {
                        artifactId 'maven-javadoc-plugin'
                        version '2.10.4'
                        executions {
                            execution {
                                id 'test'
                                phase 'install'
                            }
                        }
                    }
                }
            }
        }
    }
}