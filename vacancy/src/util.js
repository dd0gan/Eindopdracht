import { useParams } from 'react-router-dom';
import { useNavigate } from 'react-router-dom';

export function withReactRouter(Children){
    return(props)=>{

        const match  = {params: useParams()};
        const navigate = useNavigate();
        return <Children {...props}  match = {match} navigate={navigate}/>
    }
}
