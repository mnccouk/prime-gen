import { useState, useEffect, useCallback } from "react";

import './PrimeNumbers.css';


function PrimeNumbers(){

    //store state of inputs
    const [fromNumber,setFromNumber] = useState(50); //From number - Generate primes from this number that are less or eaqual 
    const [pageNumber,setPageNumber] = useState(1);  // Current page number
    const [itemsPerPage,setItemsPerPage] = useState(20); // Number of primes per page
    const [isLoading,setIsLoading] = useState(true); //Are we still loading data from the API call
    const [primesList,setPrimesList] = useState([]); // The list of prime numbers from the API call to display
    const [error, setError] = useState(null); // Any error we save here.

    //Fired when user changes the items per page
    const itemsPerPageHandler = (event) => {
        setItemsPerPage(event.target.value);
    }
    
    //Fired when the form is submitted, after clicking the 'Get Primes' button
    const submitHandler = (event) => {
        event.preventDefault();
        setPageNumber(1) //reset the page number
       // setSubmittedFromNumber(fromNumber);
       // console.log("The from Number is:" + event.target.elements.fromNumber.value);
        setFromNumber(event.target.elements.fromNumber.value);
       
    }

    //Page down through the prime numbers
    const previousPageHandler = (event) => {
        setPageNumber(pageNumber - 1);
        //console.log("The pageNumber is now: " + pageNumber);
    }
    
    //Page up through the prime numbers
    const nextPageHandler = (event) => {
        setPageNumber(pageNumber + 1);
        //console.log("The pageNumber is now: " + pageNumber);
    } 
    
    const fetchPrimes = useCallback(async () => {
            try{
                setIsLoading(true);
                //console.log('Fired get primes');
                setError(null); //clear the error
                const response = await fetch("/rest/getprimes?fromNumber=" + fromNumber + "&pageNumber=" + pageNumber + "&itemsPerPage=" + itemsPerPage);
                const primeData = await response.json();
                if (!response.ok){
                    throw Error( response.statusText + " msg:" + primeData.msg );
                }
                setIsLoading(false);
                setPrimesList(primeData.primes);
                //console.log(primeData.primes);
                

            }catch(error){
                setIsLoading(false);
                setError(error.message)
            }
        },[pageNumber,itemsPerPage,fromNumber]);
    
        
        useEffect(() => { fetchPrimes() },[fetchPrimes]);

    return (
        <div>
            <form onSubmit={submitHandler}>
                <label htmlFor="fromNumber" >Generate Primes from:</label>
                <input size="5" name="fromNumber" min="2" max="2147483647" type="number" defaultValue="50"/>
                <input type="submit" value="Get Primes"/>
                    <br/>
                <label htmlFor="itemsPerPage">Items Per Page</label>
                <input size="5" name="itemsPerPage" min="10" max="50" type="number"  value={itemsPerPage} onChange={itemsPerPageHandler}/>
                
            </form>
            {error && <p>{error}</p>}
            {isLoading && <p>Loading Please Wait...</p>}
            <input disabled={pageNumber <= 1} type="button" value="< Page" onClick={previousPageHandler} />
            <input disabled={primesList.length != itemsPerPage} type="button" value="Page >" onClick={nextPageHandler}/>
            {primesList.map(prime => <div>{prime}</div>)}
            <input disabled={pageNumber <= 1} type="button" value="< Page" onClick={previousPageHandler} />
            <input disabled={primesList.length != itemsPerPage} type="button" value="Page >" onClick={nextPageHandler}/>
        </div>
    )
}

export default PrimeNumbers;