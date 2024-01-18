let web3;
let contract;

async function connectWallet() {
    if (window.ethereum) {
        web3 = new Web3(window.ethereum);

        try {
            await window.ethereum.enable();
            const accounts = await web3.eth.getAccounts();
            updateAccount(accounts[0]);
        } catch (error) {
            console.error('Wallet connection error:', error.message);
        }
    } else {
        alert('Please install MetaMask to use this DApp.');
    }
}

async function updateAccount(account) {
    const accountElement = document.getElementById('account');
    accountElement.textContent = `Account: ${account}`;
}

async function deposit() {
    const amount = document.getElementById('depositAmount').value;

    try {
        const accounts = await web3.eth.getAccounts();
        const account = accounts[0];

        const result = await contract.methods.deposit().send({
            from: account,
            value: web3.utils.toWei(amount, 'ether'),
        });

        console.log(result); // Log the transaction result to the console
        alert('Deposit successful!');
    } catch (error) {
        console.error('Deposit error:', error.message); // Log the error message to the console
        alert(`Deposit failed: ${error.message}`);
    }
}

async function withdraw() {
    const amount = document.getElementById('withdrawAmount').value;

    try {
        const accounts = await web3.eth.getAccounts();
        const account = accounts[0];

        const result = await contract.methods.withdraw(web3.utils.toWei(amount, 'ether')).send({
            from: account,
        });

        console.log(result); // Log the transaction result to the console
        alert('Withdrawal successful!');
    } catch (error) {
        console.error('Withdrawal error:', error.message); // Log the error message to the console
        alert(`Withdrawal failed: ${error.message}`);
    }
}

async function updateContractInfo() {
    try {
        const prizePool = await contract.methods.getPrizePool().call();
        const lastDrawingTime = await contract.methods.lastDrawingTime().call();

        document.getElementById('prizePool').textContent = `Prize Pool: ${web3.utils.fromWei(prizePool, 'ether')} ETH`;
        document.getElementById('lastDrawingTime').textContent = `Last Drawing Time: ${new Date(lastDrawingTime * 1000).toLocaleString()}`;
    } catch (error) {
        console.error('Contract info update error:', error.message);
    }
}

// Replace 'YOUR_CONTRACT_ADDRESS' with your actual contract address
const contractAddress = '0xa080209080Fd92d3450382fcdA7acA5c817b37c4';
const contractABI = [
    // Paste your ABI here
];

contract = new web3.eth.Contract(contractABI, contractAddress);

// Initial contract info update
updateContractInfo();
