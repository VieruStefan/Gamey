db = db.getSiblingDB('gamey');
db.createUser({
    user: "user",
    pwd: "user",
    roles: [
        { role: "read", db: "gamey" }
    ]
});
db.createUser({
    user: "admin",
    pwd: "admin",
    roles: [
        { role: "readWrite", db: "gamey" }
    ]
});
db.createCollection('products');
db.createCollection('jobs');
db.products.createIndex({ "url": 1, "jobId": 1 }, { unique: true })
db = db.getSiblingDB('admin');
db.createUser({
    user: 'exporter',
    pwd: 'exporterpass',
    roles: [
        { role: 'read', db: 'admin' },
        { role: 'clusterMonitor', db: 'admin' },
        { role: 'read', db: 'local' },
        { role: 'readAnyDatabase', db: 'admin' }
    ]
});