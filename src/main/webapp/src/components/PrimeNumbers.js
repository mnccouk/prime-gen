import { useState, useEffect, useCallback } from "react";

function PrimeNumbers(){

    //store state of inputs
    const [fromNumber,setFromNumber] = useState(50);
    const [submittedFromNumber,setSubmittedFromNumber] = useState(50);
    const [pageNumber,setPageNumber] = useState(1);
    const [itemsPerPage,setItemsPerPage] = useState(20);
    const [primesList,setPrimesList] = useState([]);
    const [error, setError] = useState(null);

    const fromNumberChangeHandler = (event) => {
        setFromNumber(event.target.value);
    }

    const itemsPerPageHandler = (event) => {
        setItemsPerPage(event.target.value);
    }

    const pageNumberChangeHandler = (event) => {
        setPageNumber(event.target.value);
    }
    
    const submitHandler = (event) => {
        event.preventDefault();
        setPageNumber(1) //reset the page number
        setSubmittedFromNumber(fromNumber);
       
    }

    const previousPageHandler = (event) => {
        setPageNumber(pageNumber - 1);
        console.log("The pageNumber is now: " + pageNumber);
    }
    
    const nextPageHandler = (event) => {
        setPageNumber(pageNumber + 1);
        console.log("The pageNumber is now: " + pageNumber);
    } 
    
    const fetchPrimes = useCallback(async () => {
            try{
                console.log('Fired get primes');
                setError(null); //clear the error
                const response = await fetch("/rest/getprimes?fromNumber=" + fromNumber + "&pageNumber=" + pageNumber + "&itemsPerPage=" + itemsPerPage);
                const primeData = await response.json();
                if (!response.ok){
                    throw Error( response.statusText + " msg:" + primeData.msg );
                }
                 setPrimesList(primeData.primes);
                console.log(primeData.primes);

            }catch(error){
                setError(error.message)
            }
        },[pageNumber,itemsPerPage,submittedFromNumber]);
    
        
        useEffect(() => { fetchPrimes() },[fetchPrimes]);
    
   

    return (
        <div>
            <form onSubmit={submitHandler}>
                <label htmlFor="fromNumber" >Generate Primes from:</label>
                <input size="5" name="fromNumber" min="2" max="2147483647" type="text" value={fromNumber} onChange={fromNumberChangeHandler}/>
                <input type="submit" value="Get Primes"/>
                    <br/>
                <label htmlFor="itemsPerPage">Items Per Page</label>
                <input size="5" name="itemsPerPage" min="10" max="50" type="text"  value={itemsPerPage} onChange={itemsPerPageHandler}/>
                
            </form>
            {error && <p>{error}</p>}
            {primesList.map(prime => <div>{prime}</div>)}
            <input disabled={pageNumber <= 1} type="button" value="< Page" onClick={previousPageHandler} />
            <input disabled={primesList.length != itemsPerPage} type="button" value="Page >" onClick={nextPageHandler}/>
        </div>
    )
}

export default PrimeNumbers;