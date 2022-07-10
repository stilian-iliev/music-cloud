async function request(url, methodd='get', data){
    try {
        const res = await fetch(url, {method: methodd, body: data});
        
        if (res.status == 200) {
            const data = res.json();
            if (data) return data;
            return;
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