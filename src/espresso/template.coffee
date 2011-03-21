# support for templates, cheating by using java libraries

global.markdown = (name) ->
  '' + Packages.espresso.web.template.Templates.markdown(name)

global.mvel = (name, context) ->
  '' + Packages.espresso.web.template.Templates.mvel(name, context)

