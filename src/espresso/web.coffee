# web routes and dispatching.

routes = {}

global.match = (pattern, handler) ->
  routes[pattern] = handler

global.http_dispatch = () ->
  handler = routes[request.uri]
  if not handler?
    response.setStatus 404, 'Not Found'
  else
    dispatch = handler[request.method]
    if not dispatch?
      response.setStatus 405, 'Method Not Allowed'
    else
      result = dispatch()
      type = typeof result
      switch type
        when 'string' then response.setBody result
        when 'number' then response.setStatus result
        when 'object'
          response.setStatus(result.status) if result.status?
          response.setBody(result.body) if result.body?
          response.setHeader(result.content_type) if result.content_type?
          if result.headers?
            for name, value of result.headers
              response.addHeader name, value