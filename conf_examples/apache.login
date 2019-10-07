    # XXX: login server MUST be launched first
    ProxyPass /  http://localhost:$privateHttpPort/
    ProxyPassReverse / http://localhost:$privateHttpPort/
