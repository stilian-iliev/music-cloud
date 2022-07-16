const csrfToken = document.cookie.replace(/(?:(?:^|.*;\s*)XSRF-TOKEN\s*\=\s*([^;]*).*$)|^.*$/, '$1');

async function request(url, method='get', data){
    const options = {method, headers: {}};
    options.headers = { 'X-XSRF-TOKEN': csrfToken };
    if (data != undefined) {
        options['body'] = data;     
    }
    try {
        const res = await fetch(url, options);
        
        if (res.status == 200) {
            const data = res.json();
            if (data) return data;
        }
        return res;
    } catch(err) {
        // alert(err.message);
        // window.location.replace("/error");
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


