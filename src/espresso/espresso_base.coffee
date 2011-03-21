# basic platform include.

# global context (necessary for registering stuff in the global scope)
global = this
global.espresso = {}

console =
  log: (text) ->
    java.lang.System.out.println('' + text)

  __out: ->
    java.lang.System.out

require = (module) ->
  # don't include what we have already included
  if require[module]?
    return
  require[module] = true
  js = espresso_core.fetchLib(module)

  # wrap js in function to force evaluation
  eval( "(function(global) { #{js} })(this);" )

espresso.logger = (name) ->
  Packages.org.slf4j.LoggerFactory.getLogger(name)

exit = (code) ->
  java.lang.System.exit(code ?= 0)

flags = ->
  Packages.espresso.Options.asFlags()