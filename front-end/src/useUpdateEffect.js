import { useEffect, useRef } from 'react';

function useUpdateEffect(callback, deps) {

    const ref = useRef(false);

    useEffect(() => {
        if (ref.current) {
        callback();
        } else {
        ref.current = true;
        }
    }, [...deps]);
}

export default useUpdateEffect;