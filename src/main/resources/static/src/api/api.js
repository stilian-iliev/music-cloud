async function request(url, method='get', data){
    const options = {method, headers: {}};
    if (data != undefined) {
        options['headers']['Content-Type'] = 'application/json';
        options['body'] = JSON.stringify(data);   
    }

    try {
        const res = await fetch(url, options);
        if (!res.ok) {
            const error = await res.json();
            throw new Error(error.message);
        }
        if (res.status != 204) {
            const data = res.json();
            return data;
        }
    } catch(err) {
        alert(err.message);
        throw err;
    }
}

export async function get(url) {
    return request(url);
}

export async function post(url, data) {
    return request(url, 'post', data)
}

export async function put(url, data){
    return request(url, 'put', data);
}

export async function del(url) {
    return request(url, 'delete');
}